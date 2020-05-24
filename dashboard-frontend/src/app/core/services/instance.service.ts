import { Injectable } from '@angular/core';
import { map } from 'rxjs/internal/operators/map';
import { Observable } from 'rxjs';

import { CryptoService } from './crypto.service';
import { UserApiService } from './api/user-api.service';
import { CredentialsRequest } from '../models/requests/credentials-request';
import { CredentialsResponse } from '../models/responses/credentials-response';
import { Credentials } from '../models/credentials';

@Injectable({ providedIn: 'root' })
export class InstanceService {
    constructor(
        private userApiService: UserApiService,
        private cryptoService: CryptoService
    ) {}

    public loginToInstance(instanceId: string): Observable<Credentials> {
        const keyPair = this.cryptoService.generateKeys();
        const publicKeyHex: string = this.cryptoService.getPublicKeyHex(
            keyPair.publicKey
        );
        const credentials: CredentialsRequest = {
            publicKey: publicKeyHex,
        } as CredentialsRequest;

        return this.userApiService.getCredentials(instanceId, credentials).pipe(
            map((response: CredentialsResponse) => {
                const asciiPassword = this.hexToAscii(
                    response.encryptedPassword
                );
                const decryptedPassowrd = this.cryptoService.decrypt(
                    keyPair.privateKey,
                    asciiPassword
                );

                return new Credentials(response.login, decryptedPassowrd);
            })
        );
    }

    private hexToAscii(hex: string): string {
        const hexString = hex.toString();
        let asciiString = '';
        for (let n = 0; n < hexString.length; n += 2) {
            asciiString += String.fromCharCode(
                parseInt(hexString.substr(n, 2), 16)
            );
        }
        return asciiString;
    }
}
