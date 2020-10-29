import { Component } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { AccountService } from './account.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './app-navbar.component.html'
})
export class AppNavbarComponent {

  loginUser: Subject<string>;

  constructor(private accountService: AccountService) {
    this.loginUser = accountService.loginUser;
  }

  hiddenLogoutMenu(): Observable<boolean> {
    return this.loginUser.pipe(map(u => u === ''))
  }

  logout() {
    this.accountService.logout();
  }

  perm(name: string): Observable<boolean> {
    return this.accountService.hasPerm(name).pipe(map(b => !b));
  }

  perms(name: string): Observable<boolean> {
    return this.accountService.hasPermOrChild(name).pipe(map(b => !b));
  }

}
