/**
 * Measures the cost of ranking names against a query, with the fuzzy match
 * written without splitting the name.
 *
 * The search box re-ranks on every keystroke, so the ranking path is a hot
 * path. Splitting a name into words allocates an array and a string per word
 * for every candidate, which is avoidable: a word boundary is a character that
 * cannot be inside a word.
 *
 * Usage: node scripts/benchmark-search.mjs
 */
import { performance } from "node:perf_hooks";

/** The names the app ranks, in the same shape the domain model uses. */
const NAMES = [
  "Albedo", "Kaedehara Kazuha", "Raiden Shogun", "Zhongli", "Nahida",
  "Xiangling", "Xingqiu", "Bennett", "Traveler (Anemo)", "Traveler (Geo)",
  "Traveler (Hydro)", "Traveler (Pyro)", "Traveler (Cryo)", "Traveler (Electro)",
  "Traveler (Dendro)", "Jean", "Lisa", "Amber", "Klee", "Barbara",
  "Venti", "Razor", "Noelle", "Sucrose", "Kaeya", "Diluc", "Fischl", "Mona",
  "Qiqi", "Keqing",
];

/** The scoring the domain uses, copied so the benchmark measures it alone. */
function score(name, query) {
  const normalizedName = name.toLowerCase();
  if (normalizedName === query) return 4;
  if (normalizedName.startsWith(query)) return 3;
  if (normalizedName.includes(query)) return 2;
  return isFuzzyPrefix(query, normalizedName) ? 1 : 0;
}

/** Characters that end a word, so a word can be read without splitting. */
function isBoundary(character) {
  return character === " " || character === "-" || character === "'";
}

/**
 * True when the query is a prefix of any word in the name, or a word in the
 * name is a prefix of the query.
 */
function isFuzzyPrefix(query, name) {
  if (query.length === 0) return false;

  let wordStart = 0;
  while (wordStart < name.length) {
    let wordEnd = wordStart;
    while (wordEnd < name.length && !isBoundary(name[wordEnd])) {
      wordEnd += 1;
    }
    if (matches(query, name, wordStart, wordEnd)) return true;
    wordStart = wordEnd + 1;
  }
  return false;
}

/** Compares the query with the word between [start, end) without copying it. */
function matches(query, name, start, end) {
  const length = end - start;
  if (length === 0) return false;

  const shorter = length <= query.length ? length : query.length;
  for (let offset = 0; offset < shorter; offset += 1) {
    if (name[start + offset] !== query[offset]) return false;
  }
  if (length <= query.length) return true;

  // The word is longer than the query, so the query has to be its prefix.
  for (let offset = 0; offset < query.length; offset += 1) {
    if (name[start + offset] !== query[offset]) return false;
  }
  return true;
}

const QUERIES = ["a", "ka", "traveler", "bennett", "zh"];
const ROUNDS = 2000;

const started = performance.now();
let hits = 0;
for (let round = 0; round < ROUNDS; round += 1) {
  for (const query of QUERIES) {
    for (const name of NAMES) {
      if (score(name, query.toLowerCase()) > 0) hits += 1;
    }
  }
}
const elapsed = performance.now() - started;

console.log(`names: ${NAMES.length}, queries: ${QUERIES.length}, rounds: ${ROUNDS}`);
console.log(`scored pairs: ${NAMES.length * QUERIES.length * ROUNDS}`);
console.log(`total: ${elapsed.toFixed(1)} ms`);
console.log(`per round: ${(elapsed / ROUNDS).toFixed(3)} ms`);
console.log(`per pair: ${((elapsed * 1000) / (NAMES.length * QUERIES.length * ROUNDS)).toFixed(3)} us`);
console.log(`matches: ${hits}`);
