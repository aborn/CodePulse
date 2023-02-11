import { LEVELMAP } from '../common/consts';
import { Logger } from '../common/logger';
export class ValidateUtils {

    public static isLegalFileName(fileName: string): boolean {
        if (fileName
            && fileName.trim().startsWith("extension-output")
            && (!fileName.trim().includes("."))) {
            return false;
        }

        return true;
    }

    public static validate(key: string, value: string) {
        const validate = ValidateUtils.validateFn(key);
        return validate(value);
    }

    public static validateFn(key: string): Function {
        if (key === 'token') {
            return ValidateUtils.validateToken;
        } else if (key === 'url') {
            return ValidateUtils.isValidHttpUrl;
        } else {
            return (value: string) => {
                return ValidateUtils.validateOthers(key, value);
            };
        }
    }

    public static validateOthers(key: string, value: string): string {
        const err = 'level' === key ?
            `Invalid ${key}, log level can only be one of: ${Object.keys(LEVELMAP).join(',')}` :
            `Invalid ${key}... check for your ${key}`;
        if (!value) {
            return err;
        }

        const re = new RegExp(
            // 合法的id：字母、数字、下划线且长度为[1，1024]之间
            '^[a-zA-Z0-9]{1,1024}$', 'i');
        if (!re.test(value)) {
            return err;
        }

        if ('level' === key && LEVELMAP[value] === undefined) {
            return err;
        }

        return '';
    }

    public static isValidHttpUrl(urlValue: string) {
        const err = 'Invalid url... check for your input value.';
        if (!urlValue) {
            return err;
        }

        let url;
        try {
            url = new URL(urlValue);
        } catch (_) {
            return err;
        }
        const isValidate = url.protocol === "http:" || url.protocol === "https:";
        return isValidate ? '' : err;
    }

    /**
     * 验证token的合法性，长度为 3~64，以0x开头
     * @param token 
     * @returns 
     */
    public static validateToken(token: string): string {
        const err = 'Invalid token... check for your token';
        if (!token) {
            return err;
        }

        const re = new RegExp(
            // 字母、数字、下划线且长度为3~64
            '^[a-zA-Z0-9]{3,64}$', 'i');
        if (!re.test(token)) {
            return err;
        }

        // 以0x开头
        const re2 = new RegExp('^0x', 'i');
        if (!re2.test(token)) {
            return err;
        }

        return '';
    }
}