import sys


def main():
  text = sys.argv[1]
  words = text.split()
  results = []
  for word in words:
    # Strip punctuation for more accurate length
    clean_word = word.strip('.,!?()[]{}":;')
    results.append(f"'{word}': {len(clean_word)} characters")
  # Output the results as plain text
  print("\n".join(results))


if __name__ == "__main__":
  main()
