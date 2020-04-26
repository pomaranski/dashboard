import { Component } from '@angular/core';

@Component({
  selector: 'ngx-home-page',
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  instances: any = [
    {
      id: 0,
      name: 'Instance A',
    },
    {
      id: 1,
      name: 'Instance B',
    },
    {
      id: 3,
      name: 'Instance C',
    },
  ];

  selectInstance(instance: any): void {
    console.log(instance);
  }
}
