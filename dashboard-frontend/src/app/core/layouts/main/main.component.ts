import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'ngx-main-layout',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
    constructor(private router: Router) {}

    ngOnInit(): void {
        this.router.navigate(['/home']);
    }
}
