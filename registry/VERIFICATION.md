# sonae authoritative-warning-source directory — Verification Workflow (G14)

Per ADR-2606091200 (sonae 備え pre-disaster preparedness/early-warning substrate) +
the G14 verified-source-only discipline. Every
`com.etzhayyim.sonae.warningSource` record in
`registry/warning-sources.edn` ships `verificationStatus =
unverified-seed`. This file documents how an entry is moved through the
three tiers — the human/Council checks that gate any downstream relay of a
warning-source entry.

> **sonae boundary (re-asserted here, IMMUTABLE)**: sonae is the **BEFORE**
> half of the disaster cycle (prevention / mitigation / preparedness / early
> warning), NOT a state-licensed early-warning entity. This directory is the
> allowlist of OFFICIAL authoritative warning issuers whose products sonae's
> `earlyWarningRelay` cell may **RELAY** (relay-only, `authoritativeSource`
> cited). **sonae issues no warning of its own (G8) and declares no
> emergency (G10 — declaration is kazaori's Council Lv6+ ≥4/7 path, not
> sonae's).** No entry may read as sonae itself being the alerting
> authority; every entry is a citation to an external official issuer.

> **R0 status (G8 honest framing)**: this is the *process spec*. **0 entries
> are verified.** All seed entries remain `unverified-seed` — they are
> best-effort public source citations authored from official references, NOT
> maintainer-confirmed contacts (drift expected, esp. agencies mid
> reorganization or with recently-changed warning products). Verification
> execution begins at R1 (Council ratification + a warning-source
> verification maintainer DID registered, per the sonae R0→R1 roadmap in
> `CLAUDE.md`).

## Tiers (`verificationStatus`)

| Tier | Meaning | Who flips it | Unlocks |
|---|---|---|---|
| `unverified-seed` | wayfinding scaffold only; best-effort public refs | (initial) | directory display with an "unverified" badge — **no relay traffic sourced from it** |
| `maintainer-verified` | a maintainer has re-checked every field against the official source within the freshness window | warning-source verification maintainer DID | eligibility as an `authoritativeSource` cite in a relayed `earlyWarningRelay` record (R1+) |
| `council-verified` | Council-reviewed; relay-only + no-declaration boundary independently re-confirmed | Council Lv6+ | inclusion in any cross-actor consumed view (e.g. kazaori `damage_assessment` intake, `preparedness_plan` sourcing) (R2+) |

`freshnessWindowDays` (currently **180**, see `warning-sources.edn`)
bounds staleness: an entry whose `lastVerified` is older than the window is
treated as unverified for relay even if its status is `maintainer-verified`.

## Per-field verification checklist (unverified-seed → maintainer-verified)

For each warning-source entry, a maintainer confirms against the **official
authority source** (the `provenance` / `accessUrl` URL, which MUST resolve
to the agency's own official domain — never an aggregator/blog as the *sole*
basis):

1. **`title`** — matches the issuing agency's official name (incl.
   local-language form where applicable).
2. **`jurisdiction`** — the ISO-style code is correct, and (for `intl-*`)
   the body is a genuine international/multilateral or regional warning
   coordination body, not a national one mislabeled.
3. **`sourceKind`** — correctly classifies the entry within the closed
   taxonomy pinned by `test_sonae_warning_sources_seed.py`
   (`national-seismic-tsunami-authority`, `national-seismic-network`,
   `national-met-service`, `regional-tsunami-warning-center`,
   `multi-hazard-alert-aggregator`, `national-disaster-management-agency`).
4. **`organization`** — the named ministry/agency currently owns this
   warning function (re-check after government reorganizations).
5. **`accessUrl`** / **`provenance`** — both resolve, are http(s), and point
   at the agency's own official public entry point (not a dead link, not a
   parked/aggregator domain). Where a seed entry currently cites a secondary
   source, verification MUST upgrade it to the agency's own official URL
   before flipping the tier.
6. **G8 relay-only re-check (CRITICAL)** — `isAuthoritativeIssuer` is
   `true` for this entry, and its `notes` continue to re-assert that sonae
   **relays** this source's product and **originates nothing** (the literal
   words "relay" + "G8" must appear, per the machine floor below). If an
   entry cannot honestly carry `isAuthoritativeIssuer=true` (e.g. it is a
   secondary aggregator with no official standing), it MUST NOT be
   flipped — either correct the field or remove the entry.
7. **G10 no-declaration re-check** — nothing in `hazards` /
   `warningProducts` / `notes` implies sonae itself declares an emergency;
   declaration authority stays exclusively with kazaori's Council Lv6+ ≥4/7
   path.
8. **`hazards` + `warningProducts`** — accurately describe the issuer's
   actual scope and product line; do not overstate (G8 non-fabrication) — a
   coordination/standards body (e.g. IOC-UNESCO ITIC) must not be described
   as a direct public alerter.
9. **`authoritativeSourceToken`** — is a value admitted by the
   `earlyWarningRelay` Lexicon's `authoritativeSource` enum (cross-checked
   by `test_g8_registry_covers_lexicon_vocabulary` /
   `test_registry_tokens_within_lexicon_vocabulary`); a token outside the
   lexicon vocabulary is a fail-closed defect, not a stylistic choice.
10. **`lastVerified`** — set to the verification datetime (UTC, ISO-8601
    Zulu).

Only when **all 10** pass may a maintainer set `verificationStatus =
maintainer-verified` + refresh `lastVerified`.

## maintainer-verified → council-verified

Additional to the above, for an entry to be surfaced in any cross-actor
consumed view (e.g. kazaori intake, `preparedness_plan` sourcing):

- Council Lv6+ independent re-confirmation of the **relay-only** status
  (item 6) and the **no-declaration** boundary (item 7) — the two
  invariants that, if breached, would put sonae outside its constitutional
  discipline (G8 / G10); and
- a recorded Council gate reference. Note that sonae's own downstream
  handoff (`sonae_handoff_trigger` → kazaori `emergency_declaration`)
  remains a *recommendation*, never a declaration, independent of this
  registry tier.

## Current seed status (2026-07-10)

All 20 seed entries in `warning-sources.edn` are `unverified-seed`;
**0 verified** (G8 honest). Every entry carries a non-empty `accessUrl` +
`provenance` (http(s)) + ISO-8601 `lastVerified`, a `sourceKind` in the
allowed authoritative taxonomy, a `jurisdiction` (spanning 16 distinct
jurisdictions/regions worldwide, incl. `intl-*` bodies), `isAuthoritativeIssuer
= true`, and a `notes` field re-asserting the relay-only + G8 boundary.
None are authoritative contacts yet — they are routing scaffolds for the
`earlyWarningRelay` cell's future relay traffic.

## Machine-enforced floor

`70-tools/scripts/audit/test_sonae_warning_sources_seed.py` (in the
`etzhayyim/root` superproject) pins (fail-closed): the file parses +
`sources` non-empty; `sourceId` unique; **every** entry
`unverified-seed` (G14); every entry has a non-empty http(s) `accessUrl` +
`provenance` + ISO-8601 `lastVerified`; ≥12 distinct jurisdictions;
every `sourceKind` in the allowed authoritative taxonomy; every entry
`isAuthoritativeIssuer=true` with `notes` re-asserting the relay-only
boundary (mentions "relay" + "G8"); a top-level integer
`freshnessWindowDays`; every non-catch-all `authoritativeSource` token in
the `earlyWarningRelay` Lexicon is realized by some registry entry
(vocabulary coverage); and every registry `authoritativeSourceToken` is
itself within the Lexicon's enum (no drift outside the vocabulary). A seed
shipped pre-verified, missing a citation, drifting out of the taxonomy, or
dropping the relay-only re-assertion fails CI. This is the **machine
floor**; the human checklist above is the verification ceiling. The R0
routing/operational refusal itself lives in the sonae cells (R0:
import-RuntimeError, per `CLAUDE.md`).
