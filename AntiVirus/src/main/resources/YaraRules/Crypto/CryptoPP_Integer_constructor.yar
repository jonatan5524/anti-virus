rule CryptoPP_ApplyFunction
{	meta:
		author = "Maxx"
		description = "CryptoPP ApplyFunction"
	strings:
		$c0 = { 51 8D 41 E4 56 8B 74 24 0C 83 C1 F0 50 51 8B 4C 24 18 C7 44 24 0C 00 00 00 00 51 56 E8 ?? ?? ?? ?? 83 C4 10 8B C6 5E 59 C2 08 00 }
		$c1 = { 51 53 56 8B F1 57 6A 00 C7 44 24 10 00 00 00 00 8B 46 04 8B 48 04 8B 5C 31 04 8D 7C 31 04 E8 ?? ?? ?? ?? 50 8B CF FF 53 10 8B 44 24 18 8D 56 08 83 C6 1C 52 56 8B 74 24 1C 50 56 E8 ?? ?? ?? ?? 83 C4 10 8B C6 5F 5E 5B 59 C2 08 00 }
	condition:
		any of them
}