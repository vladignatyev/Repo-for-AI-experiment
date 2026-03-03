# Session: 2026-03-03 13:49:37 UTC

- **Session Key**: agent:main:main
- **Session ID**: 5f0327a9-5b12-44bf-8bc0-ea70046fb9c1
- **Source**: webchat

## Conversation Summary

user: [Sun 2026-03-01 22:03 GMT+8] openclaw how to create agent?
assistant: To create a new **agent** in OpenClaw (a fully isolated “brain” with its own workspace + auth + sessions), use the **agents** CLI.

## 1) Create an agent (wizard)
```bash
openclaw agents add work
```

Optional: pick a custom workspace location:
```bash
openclaw agents add work --workspace ~/.openclaw/workspace-work
```

Then verify:
```bash
openclaw agents list
```

## 2) Route messages to that agent (bindings)
Bindings decide which inbound messages go to which agent.

Example: bind a Telegram account named `ops` to agent `work`:
```bash
openclaw agents bind --agent work --bind telegram:ops
```

See current bindings:
```bash
openclaw agents bindings
openclaw agents list --bindings
```

## 3) Restart gateway (if needed) + check channels
```bash
openclaw gateway restart
openclaw channels status --probe
```

## Notes / mental model
- Default single-agent setup is `agentId=main` with workspace `~/.openclaw/workspace`.
- Each agent has its own state/auth dir under `~/.openclaw/agents/<agentId>/...` (so accounts/tokens aren’t automatically shared).

If you tell me which channel you want the new agent on (Telegram/Discord/WhatsApp/etc.) and whether it should get **all** messages on that channel or only a **specific DM/group**, I can give you the exact binding commands/config snippet.
