import { Component } from '@angular/core'
import { FormDataEvent } from '@angular/forms/esm2015'
import { FormGroup, FormControl, Validators } from '@angular/forms'
import { PageEvent } from '@angular/material/paginator'
import { NgbModal } from '@ng-bootstrap/ng-bootstrap'
import { UsersService } from './users.service'
import { ConfirmDialog } from '../utils/confirm.dialog'
import { MessageNotify } from '../utils/message-notify'
import { AccountService } from '../account/account.service'
import { User } from './users.entity'
import { Page } from '../app.entity'
import { UserRolesDialog } from './user-roles.dialog'
import { UserGroupsDialog } from './user-groups.dialog'
import { NewUserDialog } from './new-user.dialog'

@Component({
    selector: 'users',
    templateUrl: './users.component.html'
})
export class UsersComponent {
    pageSize: number = 8
    userList: Page<User> = new Page()
    searchForm: FormGroup = new FormGroup({
        searchPrefix: new FormControl('', [Validators.required]),
    })
    public openRegistry: FormControl = new FormControl('')

    constructor(
            public svc: UsersService,
            public account: AccountService,
            protected alert: MessageNotify,
            protected modal: NgbModal) {
        this.query(0, '')
        this.svc.getOpenRegister().subscribe(ret => {
            if (ret.code == 0) {
                this.openRegistry.setValue(ret.result)
            }
        })
    }

    public onPageEvent(event: PageEvent): PageEvent {
        let page = event.pageIndex + 1
        this.pageSize = event.pageSize
        let search = this.searchForm.get('searchPrefix').value
        this.query(page, search)
        return event
    }

    query(page:number, nameSearch: string) {
        this.svc.getUsers(page, this.pageSize, nameSearch).subscribe(ret => {
            if (ret.code == 0) {
                this.userList = ret.result
            }
        })
    }

    search(event: FormDataEvent) {
        event.preventDefault()
        let pre = this.searchForm.get('searchPrefix').value
        this.query(0, pre)
    }

    onNewUserBtnClick() {
        this.modal.open(NewUserDialog).result.then(ret => {
            if (ret.id) {
                this.userList.items.push(ret)
                this.alert.success('新建用户成功')
            }
        },reason => {})
    }

    onUserRolesBtnClick(user: User) {
        let ref = this.modal.open(UserRolesDialog, {size: 'lg'})
        ref.componentInstance.user = user
        ref.result.then(result => {
            if (result == 'ok') {
                this.alert.success('修改用户角色成功')
            }
        }, reason => {})
    }

    onUserGroupsBtnClick(user: User) {
        let ref = this.modal.open(UserGroupsDialog, {size: 'lg'})
        ref.componentInstance.user = user
        ref.result.then(result => {
            if (result == 'ok') {
                this.alert.success('修改用户分组成功')
            }
        }, reason => {})
    }

    onResetBtnClick(user: User) {
        let ref = this.modal.open(ConfirmDialog)
        ref.componentInstance.title = "确认要重置吗?"
        ref.componentInstance.message = "重置 [" + user.name + "] 的密码"
        ref.result.then(result => {
            if (result == "ok") {
                this.svc.resetUserPassword(user.id).subscribe(ret => {
                    if (ret.code == 0) {
                        let ref = this.modal.open(ConfirmDialog)
                        ref.componentInstance.title = "重置密码成功!"
                        ref.componentInstance.message = "临时密码为 [" + ret.result + "]"
                        ref.componentInstance.cancel = ''
                    }
                })
            }
        }, resaon => {})
    }

    onDelBtnClick(user: User) {
        let ref = this.modal.open(ConfirmDialog)
        ref.componentInstance.title  = "确认要删除吗?"
        ref.componentInstance.message= "删除账号: " + user.name
        ref.componentInstance.detail = "此操作将删除账号'" + user.name + "',此操作不可恢复请谨慎操作！"
        ref.result.then(result => {
            if (result == "ok") {
                this.svc.deleteUser(user).subscribe(ret => {
                    if (ret.code == 0) {
                        this.userList.items = this.userList.items.filter(it => it.id != user.id)
                        this.alert.success('删除账号成功')
                    }
                })
            }
        }, resaon => { })
    }

    onUnblockBtnClick(user: User) {
        let ref = this.modal.open(ConfirmDialog)
        ref.componentInstance.title = "启用账号: " + user.name
        ref.componentInstance.message = "确认要启用吗?"
        ref.componentInstance.detail = "此操作将启用账号'" + user.name + "',用户可以正常登陆并访问或操作被授权的资源"
        ref.result.then(result => {
            if (result == "ok") {
                this.svc.unblockUser(user).subscribe(ret => {
                    if (ret.code == 0) {
                        user.locked = false
                        this.alert.success('启用账号成功')
                    }
                })
            }
        }, resaon => { })
    }

    onBlockBtnClick(user: User) {
        let ref = this.modal.open(ConfirmDialog)
        ref.componentInstance.title = "确认要禁用吗?"
        ref.componentInstance.message = "禁用账号: " + user.name
        ref.componentInstance.detail = "此操作将把账号'" + user.name + "'标记为禁用状态，不会直接删除用户及其相关数据"
        ref.result.then(result => {
            if (result == "ok") {
                this.svc.blockUser(user).subscribe(ret => {
                    if (ret.code == 0) {
                        user.locked = true
                        this.alert.success('禁用账号成功')
                    }
                })
            }
        }, resaon => { })
    }

    onUpdateRegStatus(event: FormDataEvent) {
        event.preventDefault()
        let v = this.openRegistry.value
        let ref = this.modal.open(ConfirmDialog)
        ref.componentInstance.title = v ? "确认要关闭注册吗？" : "确认要开放注册吗？"
        ref.componentInstance.message = v ? "此操作将关闭注册，用户必须通过管理员创建账号" : "此操作将开放注册，用户可以自行注册账号"
        ref.result.then(result => {
            if (result == "ok") {
                v = !v
                this.svc.setOpenRegister(v).subscribe(ret => {
                    if (ret.code == 0) {
                        this.openRegistry.setValue(v)
                        this.alert.success(v?'已开放注册':'已关闭注册')
                    }
                })
            }
        }, reason => {})
    }
}
