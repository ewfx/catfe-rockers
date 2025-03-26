import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TestCaseService } from '../../services/test-case.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-test-case-generator',
  templateUrl: './test-case-generator.component.html',
  styleUrls: ['./test-case-generator.component.css']
})
export class TestCaseGeneratorComponent {
  testCaseForm: FormGroup;
  testCases: string[] = [];
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private testCaseService: TestCaseService,
    private snackBar: MatSnackBar
  ) {
    this.testCaseForm = this.fb.group({
      paymentType: ['Credit Card', Validators.required],
      count: [5, [Validators.required, Validators.min(1), Validators.max(20)]],
      requirements: ['']
    });
  }

  generateTestCases() {
    if (this.testCaseForm.invalid) {
      return;
    }

    this.isLoading = true;
    const { paymentType, count, requirements } = this.testCaseForm.value;

    this.testCaseService.generateTestCases(paymentType, count, requirements)
      .subscribe({
        next: (response) => {
          this.testCases = response;
          this.isLoading = false;
        },
        error: (error) => {
          this.snackBar.open('Error generating test cases: ' + error.message, 'Close', {
            duration: 5000
          });
          this.isLoading = false;
        }
      });
  }
}
