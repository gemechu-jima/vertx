import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ItemService } from './services/item.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CRUD Items Manager';
  items: any[] = [];
  newItem = { name: '', description: '' };
  editingItem: any = null;
  loading = false;

  constructor(private itemService: ItemService) {
    this.loadItems();
  }

  loadItems() {
    this.loading = true;
    this.itemService.getItems().subscribe({
      next: (data) => {
        this.items = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading items', err);
        this.loading = false;
      }
    });
  }

  addItem() {
    if (!this.newItem.name.trim()) {
      alert('Please enter a name');
      return;
    }

    this.itemService.createItem(this.newItem).subscribe({
      next: (item) => {
        this.items.push(item);
        this.newItem = { name: '', description: '' };
        alert('Item created successfully!');
      },
      error: (err) => {
        console.error('Error creating item', err);
        alert('Failed to create item');
      }
    });
  }

  editItem(item: any) {
    this.editingItem = { ...item };
  }

  saveEdit() {
    if (!this.editingItem) return;

    this.itemService.updateItem(this.editingItem.id, this.editingItem).subscribe({
      next: (updated) => {
        const index = this.items.findIndex(i => i.id === updated.id);
        if (index > -1) {
          this.items[index] = updated;
        }
        this.editingItem = null;
        alert('Item updated successfully!');
      },
      error: (err) => {
        console.error('Error updating item', err);
        alert('Failed to update item');
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('Are you sure you want to delete this item?')) {
      this.itemService.deleteItem(id).subscribe({
        next: () => {
          this.items = this.items.filter(i => i.id !== id);
          alert('Item deleted successfully!');
        },
        error: (err) => {
          console.error('Error deleting item', err);
          alert('Failed to delete item');
        }
      });
    }
  }

  cancelEdit() {
    this.editingItem = null;
  }
}
