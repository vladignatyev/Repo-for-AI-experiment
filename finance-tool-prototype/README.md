# Finance Tool Prototype (HTML)

Static prototype for a consumer Android finance app aimed at mass-market investors.

## Included calculators

- FIRE calculator (timeline to FI)
- “How much to retire” (target portfolio from spending + SWR)
- Passive income calculator (SWR vs yield)
- Compound interest (principal + monthly contributions + simple chart)

## Run

Open `index.html` in a browser.

Optionally serve locally:

```bash
cd finance-tool-prototype
python3 -m http.server 8000
# then open http://localhost:8000
```

## Notes

- This is a simplified MVP prototype for UI/UX and basic math; not financial advice.
- Production app should include scenarios, ranges, country presets, and robust disclosures.
