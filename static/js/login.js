document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const data = { email, password };
    const result = window.loginSchema.safeParse(data);
    if (!result.success) {
        alert(result.error.errors.map(err => err.message).join('\n'));
        return;
    }

    try {
        const response = await axios.post('/users/login', data);
        alert('로그인 성공');
        // Handle success, e.g., redirect or store token
    } catch (error) {
        console.error('Error:', error);
        alert('로그인 실패');
    }
});

function loginWithSNS(provider) {
    // Implement SNS login logic here
    alert(`${provider} 로그인 구현 예정`);
}
