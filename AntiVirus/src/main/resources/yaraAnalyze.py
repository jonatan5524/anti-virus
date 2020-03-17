import sys
import os
import yara


output = ''

print(sys.argv[1])
print(sys.argv[2])
f2 = open(sys.argv[1])
print(f2.read())

rules = yara.compile(file=f2)
#matches = rules.match(sys.argv[2])

f = open(sys.argv[2],'rb')
print(f.read())
matches = rules.match(data=f.read())
f.close()
#matches = rules.match(sys.argv[2], callback=mycallback)
f2.close()
print(matches)
for match in matches:
    output += match.rule + ' '
print('output: '+output)
sys.stdout.write(output)
sys.stdout.flush()
sys.exit(0)
