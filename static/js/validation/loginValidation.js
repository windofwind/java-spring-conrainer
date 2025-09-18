// Login validation schema using Zod
const loginSchema = zod.object({
    email: zod.string().email("올바른 이메일 형식이 아닙니다"),
    password: zod.string().min(8, "비밀번호는 8자 이상이어야 합니다")
});

// Export for use in login.js
window.loginSchema = loginSchema;
