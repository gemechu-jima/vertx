import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { UserService } from './services/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  providers: [UserService],
  imports: [CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  users: any[] = [];

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
 onSubmit() {}
}