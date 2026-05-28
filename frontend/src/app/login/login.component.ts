import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './services/login.service';
import { HttpClientModule } from '@angular/common/http';
@Component({
  selector: 'app-login',
  standalone: true,
  providers:[UserService],
  imports: [ CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  title = 'Login Page';
  private userService=Inject(UserService)
  login() {
    // Implement login logic here
  }
  register() {
    // Implement registration logic here
  }
  getAllUsers() {
    this.userService.getAllUsers()
      .subscribe((data: any[]) => {

        console.log(data);

      });

  }

}
