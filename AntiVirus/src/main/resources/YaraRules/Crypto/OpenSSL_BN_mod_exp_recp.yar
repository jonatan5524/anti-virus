rule OpenSSL_BN_mod_exp2_mont
{	meta:
		author = "Maxx"
		description = "OpenSSL BN_mod_exp2_mont"
	strings:
		$c0 = { B8 30 05 00 00 E8 ?? ?? ?? ?? 8B 84 24 48 05 00 00 53 33 DB 56 8B 08 57 89 5C 24 24 89 5C 24 30 8A 01 89 5C 24 28 A8 01 89 5C 24 0C 75 24 68 89 00 00 00 68 ?? ?? ?? ?? 6A 66 6A 76 6A 03 E8 ?? ?? ?? ?? 83 C4 14 33 C0 5F 5E 5B 81 C4 30 05 00 00 C3 8B 94 24 48 05 00 00 52 E8 ?? ?? ?? ?? 8B F0 8B 84 24 54 05 00 00 50 E8 ?? ?? ?? ?? 83 C4 08 3B F3 8B F8 75 20 3B FB 75 1C 8B 8C 24 40 05 00 00 6A 01 51 E8 ?? ?? ?? ?? 83 C4 08 5F 5E 5B 81 C4 30 05 00 00 C3 3B F7 89 74 24 18 7F 04 89 }
	condition:
		$c0
}