import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        constant_load: {
            executor: 'constant-arrival-rate',
            rate: 1000,
            timeUnit: '1.5s',
            duration: '60s',
            preAllocatedVUs: 1000,
            maxVUs: 1000,
        },
    }
};

// Функция setup выполняется один раз и возвращает walletId
export function setup() {
    const createWalletRes = http.post('http://localhost:8080/api/v1/wallets', null, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(createWalletRes, {
        'create wallet status is 201': (r) => r.status === 201,
    });

    if (createWalletRes.status !== 201) {
        throw new Error('Failed to create wallet');
    }

    const wallet = createWalletRes.json();
    return wallet.walletId;
}

export default function (walletId) {
    // walletId приходит из setup

    const operationType = Math.random() < 0.5 ? 'DEPOSIT' : 'WITHDRAW';
    const amount = Math.floor(Math.random() * 1000) + 1;

    const payload = JSON.stringify({
        walletId: walletId,
        operationType: operationType,
        amount: amount,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const url = 'http://localhost:8080/api/v1/wallet';

    const res = http.post(url, payload, params);

    check(res, {
        'status is 2xx or known errors': (r) =>
            r.status >= 200 && r.status < 300 ||
            [400, 404, 409].includes(r.status),
    }) || console.log(`Unexpected status: ${res.status} - ${res.body}`);

    sleep(0.1);
}
