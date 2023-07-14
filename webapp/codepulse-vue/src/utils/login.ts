export type AuthData = {
    /**
     * 提供用于登录和授权应用程序的特定账户【可空】。
     * 注意，经过试验后，这里要填的是 GitHub 用户名，这样跳转之后会向用户建议登录该账号。
     */
    login: string
    state: string
    redirect_uri: string
}

export function loginWithGithubOauth2() {
    const data: AuthData = {
        login: 'aborn',
        state: '88bb66aa',
        redirect_uri: 'http://127.0.0.1:8001/api/v1/codepulse/oauth2/login/redirect'
    }
    githubOauthAuthorize(data);
}

export function githubOauthAuthorize(data: AuthData) {
    const query = new URLSearchParams({
        ...data,
        client_id: '2645bbcd62a78528da2a',
        scope: '',
        allow_signup: 'true',
    })
    const url = `https://github.com/login/oauth/authorize?${query.toString()}`
    window.location.href = url // 直接跳转
    // 或 window.open(url) // 或者另外开窗口
}
