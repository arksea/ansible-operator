import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { User, Role } from './users.entity'
import { ServiceResponse } from '../utils/http-utils'
import { HttpUtils } from '../utils/http-utils'
import { environment } from '../../environments/environment'

@Injectable()
export class UsersService {

    public roles: Array<Role> = []

    public constructor(private httpUtils: HttpUtils) {
        this.queryRoles()
    }

    public getUsers(type: string): Observable<ServiceResponse<Array<User>>> {
        let url = environment.apiUrl + '/api/users/' + type
        return this.httpUtils.httpGet('查询用户信息', url)
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
}
