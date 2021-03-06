import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { HttpClientModule } from '@angular/common/http'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap'
import { ConfirmDialog } from './confirm.dialog'
import { MessageNotify } from './message-notify'
import { HttpUtils } from './http-utils'

@NgModule({
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    HttpClientModule,
    NgbAlertModule, NgbModule],
  declarations: [
    ConfirmDialog
  ],
  entryComponents: [
    ConfirmDialog
  ],
  providers: [MessageNotify,HttpUtils],
  exports: []
})
export class CommonUtilsModule { }
