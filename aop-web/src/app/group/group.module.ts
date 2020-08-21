import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { VenderModule } from '../vender.module';
import { NgbPaginationModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { GroupRoutingModule } from './group-routing.module';
import { GroupsComponent } from './groups.component';
import { AccountModule } from '../account/account.module';
import { NewGroupDialog } from './new-group.dialog';
import { GroupService } from './group.service';

@NgModule({
  imports: [
    RouterModule,
    CommonModule, FormsModule, ReactiveFormsModule,
    HttpClientModule,
    VenderModule, NgbPaginationModule, NgbAlertModule, NgbModule,
    AccountModule, GroupRoutingModule],
  declarations: [
    GroupsComponent,NewGroupDialog
  ],
  entryComponents: [
    NewGroupDialog
  ],
  providers: [GroupService],
  exports: []
})
export class GroupModule { }
