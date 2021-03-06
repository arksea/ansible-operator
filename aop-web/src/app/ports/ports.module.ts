import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { HttpClientModule } from '@angular/common/http'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { VenderModule } from '../vender.module'
import { NgbPaginationModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'
import { RouterModule } from '@angular/router'
import { AccountModule } from '../account/account.module'
import { PortsRoutingModule } from './ports-routing.module'
import { PortsComponent } from './ports.component'
import { PortsService } from './ports.service'
import { EditSectionDialog } from './edit-section.dialog'
import { EditPortTypeDialog } from './edit-port-type.dialog'

@NgModule({
  imports: [
    RouterModule,
    CommonModule, FormsModule, ReactiveFormsModule,
    HttpClientModule,
    VenderModule, NgbPaginationModule, NgbAlertModule, NgbModule,
    AccountModule,PortsRoutingModule],
  declarations: [
    PortsComponent, EditSectionDialog, EditPortTypeDialog
  ],
  entryComponents: [
    EditSectionDialog, EditPortTypeDialog
  ],
  providers: [PortsService],
  exports: []
})
export class PortsModule { }
