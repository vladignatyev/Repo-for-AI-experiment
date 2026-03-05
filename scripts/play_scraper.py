#!/usr/bin/env python3
"""
Simple wrapper around google-play-scraper for two tasks:
 - fetch app metadata by app id
 - search apps by query

Writes JSON to --out file or to stdout.
"""
import argparse
import json
import sys

try:
    from google_play_scraper import app as gapp, search as gsearch
except Exception as e:
    print('Error importing google_play_scraper:', e, file=sys.stderr)
    print('Make sure virtualenv is active and package is installed: .venv/bin/python -m pip install google-play-scraper', file=sys.stderr)
    sys.exit(2)


def fetch_app(appid):
    # returns dict
    return gapp(appid, lang='en', country='us')


def search_apps(query, n=10):
    # google_play_scraper.search returns list of dicts
    return gsearch(query, lang='en', country='us', count=n)


def main():
    p = argparse.ArgumentParser()
    p.add_argument('--appid', help='App id (e.g. com.spotify.music)')
    p.add_argument('--search', help='Search query')
    p.add_argument('--limit', type=int, default=10, help='Number of search results')
    p.add_argument('--out', help='Output JSON file (default stdout)')

    args = p.parse_args()

    result = None
    if args.appid:
        result = {'task':'app','appid':args.appid, 'result': fetch_app(args.appid)}
    elif args.search:
        result = {'task':'search','query':args.search,'limit':args.limit, 'result': search_apps(args.search, n=args.limit)}
    else:
        p.print_help()
        sys.exit(1)

    out_json = json.dumps(result, indent=2, ensure_ascii=False)
    if args.out:
        with open(args.out, 'w', encoding='utf-8') as f:
            f.write(out_json)
        print('Wrote', args.out)
    else:
        print(out_json)

if __name__=='__main__':
    main()
