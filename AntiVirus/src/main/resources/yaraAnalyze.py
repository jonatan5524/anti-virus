import sys
import os
import yara

output = ''

rules = yara.compile(sys.argv[1])

matches = rules.match(sys.argv[2])

for match in matches:
    output += match.rule + ' '

sys.stdout.write(output)
sys.stdout.flush()
sys.exit(0)
