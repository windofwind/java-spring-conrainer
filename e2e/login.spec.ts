import { test, expect, Page } from '@playwright/test';

test('login page loads correctly', async ({ page }: { page: Page }) => {
  // Navigate to the login page (adjust URL as needed for your setup)
  await page.goto('http://localhost:8080/pages/login.html');

  // Check page title
  await expect(page).toHaveTitle('로그인');

  // Check main heading
  await expect(page.locator('h1')).toHaveText('로그인');

  // Check form elements
  await expect(page.locator('#email')).toBeVisible();
  await expect(page.locator('#password')).toBeVisible();
  await expect(page.locator('button[type="submit"]')).toHaveText('로그인');

  // Check SNS login buttons
  await expect(page.locator('button').filter({ hasText: '네이버' })).toBeVisible();
  await expect(page.locator('button').filter({ hasText: '카카오' })).toBeVisible();
  await expect(page.locator('button').filter({ hasText: '다음' })).toBeVisible();
  await expect(page.locator('button').filter({ hasText: 'Apple' })).toBeVisible();

  // Check signup link
  await expect(page.locator('a[href="signup.html"]')).toHaveText('회원가입');
});

test('login form validation', async ({ page }: { page: Page }) => {
  await page.goto('http://localhost:8080/pages/login.html');

  // Try to submit empty form
  await page.locator('button[type="submit"]').click();

  // Check if validation prevents submission (assuming HTML5 validation)
  // Note: This may need adjustment based on actual validation implementation
  await expect(page.locator('#email:invalid')).toBeVisible();
  await expect(page.locator('#password:invalid')).toBeVisible();
});

test('fill login form', async ({ page }: { page: Page }) => {
  await page.goto('http://localhost:8080/pages/login.html');

  // Fill email and password
  await page.fill('#email', 'test@example.com');
  await page.fill('#password', 'password123');

  // Check values are filled
  await expect(page.locator('#email')).toHaveValue('test@example.com');
  await expect(page.locator('#password')).toHaveValue('password123');
});

test('successful login API request', async ({ page }: { page: Page }) => {
  // Test successful login via API
  const response = await page.request.post('http://localhost:8080/users/login', {
    data: {
      email: 'test@example.com',
      password: 'password123'
    }
  });

  // Assuming the user exists and login succeeds
  expect(response.status()).toBe(200);
  const responseBody = await response.json();
  expect(responseBody).toHaveProperty('id');
  expect(responseBody).toHaveProperty('email', 'test@example.com');
});

test('failed login API request', async ({ page }: { page: Page }) => {
  // Test failed login with wrong credentials
  const response = await page.request.post('http://localhost:8080/users/login', {
    data: {
      email: 'wrong@example.com',
      password: 'wrongpassword'
    }
  });

  expect(response.status()).toBe(401);
});
