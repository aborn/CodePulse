
export interface ServerInfo {
    url: string;
    token: string | null;
    timeout: number;
};

export const LOCAL_SERVER: ServerInfo = {
    url: 'http://127.0.0.1:8000/codepulse/',
    token: '0x4af97337',    // this token only for test.
    timeout: 1000
};

export const REMOTE_SERVER: ServerInfo = {
    url: 'https://yourserverdomain/codepulse/',
    token: '0x4af97337',
    timeout: 2000
};