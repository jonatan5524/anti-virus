rule BigDig_spModInv
{	meta:
		author = "Maxx"
		description = "BigDig spModInv"
	strings:
		$c0 = { 51 8B 4C 24 10 55 56 BD 01 00 00 00 33 F6 57 8B 7C 24 18 89 6C 24 0C 85 C9 74 42 53 8B C7 33 D2 F7 F1 8B C7 8B F9 8B DA 33 D2 F7 F1 8B CB 0F AF C6 03 C5 8B EE 8B F0 8B 44 24 10 F7 D8 85 DB 89 44 24 10 75 D7 85 C0 5B 7D 13 8B 44 24 1C 8B 4C 24 14 2B C5 5F 89 01 5E 33 C0 5D 59 C3 8B 54 24 14 5F 5E 33 C0 89 2A 5D 59 C3 }
	condition:
		$c0
}