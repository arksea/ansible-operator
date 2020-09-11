import { Component, OnInit } from '@angular/core';
import { FormDataEvent } from '@angular/forms/esm2015';
import { FormGroup, FormControl, AbstractControl, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AppsService } from './apps.service';
import { MessageNotify } from '../utils/message-notify';
import { App, AppGroup } from '../app.entity';
import { AccountService } from '../account/account.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AppEditService } from './app-edit.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-edit',
    templateUrl: './app-edit.component.html'
})
export class AppEditComponent implements OnInit {

    public groupSelectModel: any;

    public userGroups: AppGroup[] = [];

    constructor(public svc: AppsService,
                public editSvc: AppEditService,
                public account: AccountService,
                protected alert: MessageNotify,
                protected modal: NgbModal,
                private router: Router,
                private route: ActivatedRoute) {
        let str = this.route.snapshot.paramMap.get('id');
        if (str == 'new') {
            this.svc.setSelectedApp(undefined);
        } else {
            let id = Number(str);
            this.svc.setSelectedApp(id);
        }

        let u = new AppGroup();
        u.id = 1;
        u.name = '天气产品线';
        this.userGroups.push(u);
        u = new AppGroup();
        u.id = 2;
        u.name = '桌面产品线';
        this.userGroups.push(u);
    }

    ngOnInit(): void {
    }

    public save(event: FormDataEvent) {
        event.preventDefault(); //取消submit事件的默认处理：刷新页面
        this.editSvc.opSaveApp.next(true);
    }

    public cancel() {
        this.router.navigate(['/apps'])
    }

}
