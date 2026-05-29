import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { UserService } from './services/login.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  providers: [UserService],
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  users: any[] = [];
  username: string = '';
  password: string = '';
  private userService = inject(UserService);
  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers() {
    this.userService.getAllUsers()
      .subscribe((data: any) => {
        this.users=data.users;
      });
  }
  editUser(user: any) {
  console.log("Edit user:", user);
  // later: open form or modal
}

deleteUser(user: any) {
  console.log("Delete user:", user);

  this.userService.deleteUser(user.id).subscribe({
    next: () => {
      this.users = this.users.filter(u => u.id !== user.id);
    },
    error: (err) => console.log(err)
  });
}
 login() {
  this.userService.login(this.username, this.password).subscribe({
    next: (response) => console.log('Login successful:', response),
    error: (err) => console.log('Login failed:', err)
  })
 }
 register() {
  this.userService.register(this.username, this.password).subscribe({
    next: (response) => console.log('Registration successful:', response),
    error: (err) => console.log('Registration failed:', err)
  })
 }
}