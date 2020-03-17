rule Prime_Constants_char {
	meta:
		author = "_pusher_"
		description = "List of primes [char]"
		date = "2016-07"
	strings:
		$c0 = { 03 05 07 0B 0D 11 13 17 1D 1F 25 29 2B 2F 35 3B 3D 43 47 49 4F 53 59 61 65 67 6B 6D 71 7F 83 89 8B 95 97 9D A3 A7 AD B3 B5 BF C1 C5 C7 D3 DF E3 E5 E9 EF F1 FB }
	condition:
		$c0
}