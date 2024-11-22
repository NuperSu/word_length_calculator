import sys
import json

def main():
  text = sys.argv[1]
  words = text.split()
  result = {}
  for word in words:
    # Strip punctuation for more accurate length
    clean_word = word.strip('.,!?()[]{}":;')
    result[word] = len(clean_word)
  # Output the results as JSON
  print(json.dumps(result))

if __name__ == "__main__":
  main()
