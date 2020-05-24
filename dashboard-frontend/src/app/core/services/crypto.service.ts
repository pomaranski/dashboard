import { Injectable } from '@angular/core';
import * as forge from 'node-forge';

@Injectable({ providedIn: 'root' })
export class CryptoService {
    private readonly BITS: number = 1024;

    public generateKeys(): any {
        return forge.pki.rsa.generateKeyPair(this.BITS);
    }

    public getPublicKeyHex(publicKey: any): string {
        return this.base64ToHex(
            this.removeTags(forge.pki.publicKeyToPem(publicKey))
        );
    }

    public decrypt(privateKey: any, encrypted: string): any {
        const decrypted = privateKey.decrypt(encrypted, 'RSA-OAEP', {
            md: forge.md.sha256.create(),
            mgf1: {
                md: forge.md.sha1.create(),
            },
        });

        return decrypted;
    }

    private removeTags(key: string): string {
        return key
            .replace('-----BEGIN PUBLIC KEY-----', '')
            .replace('-----END PUBLIC KEY-----', '')
            .trim();
    }

    private base64ToHex(str: string): string {
        const raw = atob(str);
        let result = '';
        for (let i = 0; i < raw.length; i++) {
            const hex = raw.charCodeAt(i).toString(16);
            result += hex.length === 2 ? hex : '0' + hex;
        }
        return result.toUpperCase();
    }
}
