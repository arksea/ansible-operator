import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { User, Role } from './users.entity'
import { Page, AppGroup } from '../app.entity'
import { ServiceResponse } from '../utils/http-utils'
import { HttpUtils } from '../utils/http-utils'
import { environment } from '../../environments/environment'

@Injectable()
export class UsersService {

    public roles: Array<Role> = []
    public groups: Array<AppGroup> = []

    public constructor(private httpUtils: HttpUtils) {
        this.queryRoles()
        this.queryGroups()
    }

    public getUsers(page: number, pageSize: number, nameSearch: string): Observable<ServiceResponse<Page<User>>> {
        let url = environment.apiUrl + '/api/users?page='+page+'&pageSize='+pageSize
        if (nameSearch != '') {
            url = url + '&nameSearch='+nameSearch
        }
        return this.httpUtils.httpGet('查询用户列表', url)
    }

    private queryRoles() {
        let url = environment.apiUrl + '/api/roles/'
        let ret: Observable<ServiceResponse<Array<Role>>> = this.httpUtils.httpGet('查询用户信息', url)
        ret.subscribe(data => {
            if (data.code == 0) {
                this.roles= data.result
            }
        })
    }

    public queryGroups() {
        const url = environment.apiUrl + '/api/groups'
        let ret: Observable<ServiceResponse<Array<AppGroup>>> = this.httpUtils.httpGet('查询组信息', url)
        ret.subscribe(data => {
            if (data.code == 0) {
                this.groups = data.result
            }
        })
    }

    public blockUser(user: User): Observable<ServiceResponse<any>> {
        const url = environment.apiUrl + '/api/users/active/' + user.id
        return this.httpUtils.httpDelete('禁用账号', url)
    }

    public unblockUser(user: User): Observable<ServiceResponse<any>> {
        const url = environment.apiUrl + '/api/users/blocked/' + user.id + '?action=unblock'
        return this.httpUtils.httpPut('启用账号', url, '')
    }

    public deleteUser(user: User): Observable<ServiceResponse<any>> {
        const url = environment.apiUrl + '/api/users/blocked/' + user.id
        return this.httpUtils.httpDelete('删除用户', url)
    }

    public updateUserRoles(userId: number, ids: Array<number>) {
        const url = environment.apiUrl + '/api/roles/user/' + userId
        return this.httpUtils.httpPut('更新用户角色列表', url, ids)
    }

    public resetUserPassword(userId: number): Observable<ServiceResponse<string>> {
        const url = environment.apiUrl + '/api/users/' + userId + '/password?action=reset'
        return this.httpUtils.httpPut('更新用户角色列表', url, '')
    }
    
    public setOpenRegister(status: boolean): Observable<ServiceResponse<any>> {
        const url = environment.apiUrl + '/api/sys/openRegistry'
        return this.httpUtils.httpPut('读取开放注册状态', url, status)
    }

    public getOpenRegister(): Observable<ServiceResponse<boolean>> {
        const url = environment.apiUrl + '/api/sys/openRegistry'
        return this.httpUtils.httpGet('读取开放注册状态', url)
    }

    public getUserGroups(userId: number): Observable<ServiceResponse<Array<AppGroup>>> {
        const url = environment.apiUrl + '/api/groups/user/'+userId
        return this.httpUtils.httpGet('读取用户加入的分组', url)
    }

    public updateUserGroups(userId: number, ids: Array<number>) {
        const url = environment.apiUrl + '/api/groups/user/' + userId
        return this.httpUtils.httpPut('更新用户加入的分组', url, ids)
    }
}
