---
name: play-scraper
description: Use google-play-scraper to fetch app metadata and search results from Google Play. Provides CLI script `play_scraper.py` in workspace/scripts.
metadata: { "openclaw": { "emoji": "🤖", "requires": { "bins": ["python3"] }, "user-invocable": true } }
---

# Play Scraper skill

This skill provides a small CLI script that uses the `google-play-scraper` Python package to:

- fetch the full app metadata (including the description) for a given app id
- search Google Play for a query and return the top N results

Files:
- `workspace/scripts/play_scraper.py` — the executable script. Run with the workspace venv python: `.venv/bin/python scripts/play_scraper.py --appid com.spotify.music`

Usage examples:
- Fetch app description:
  `.venv/bin/python scripts/play_scraper.py --appid com.spotify.music --out spotify.json`

- Search for 10 apps by query:
  `.venv/bin/python scripts/play_scraper.py --search "music player" --limit 10 --out search_music.json`

Notes:
- The skill expects a virtualenv at `./.venv/` with `google-play-scraper` installed. See workspace README for setup.
- Output is JSON written to the path passed with `--out` (defaults to stdout when omitted).

Security:
- This script only performs public read-only queries to Google Play web endpoints via the python library.
