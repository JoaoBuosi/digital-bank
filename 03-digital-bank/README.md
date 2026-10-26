# Simplified Digital Bank
`docker compose -f compose.yaml up --build` — API em `http://localhost:8083`.
- `POST /api/accounts` body `{"initialBalance":100.00}`
- `GET /api/accounts`
- `POST /api/transfers`, header `Idempotency-Key: unique-key`, body `{"fromAccount":1,"toAccount":2,"amount":10.00}`
- `GET /api/accounts/{id}/statement`
- Métricas Prometheus em `/actuator/prometheus`.
Demonstra transação e chave idempotente. Não é um core bancário: concorrência de chaves simultâneas, ledger imutável, saga/event sourcing, autenticação e regras regulatórias exigem implementação adicional antes de qualquer uso real.
