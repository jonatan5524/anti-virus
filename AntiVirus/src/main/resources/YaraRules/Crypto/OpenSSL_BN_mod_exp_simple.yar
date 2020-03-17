rule OpenSSL_BN_mod_exp_mont
{	meta:
		author = "Maxx"
		description = "OpenSSL BN_mod_exp_mont"
	strings:
		$c0 = { B8 A0 02 00 00 E8 ?? ?? ?? ?? 53 56 57 8B BC 24 BC 02 00 00 33 F6 8B 07 89 74 24 24 89 74 24 20 89 74 24 0C F6 00 01 75 24 68 72 01 00 00 68 ?? ?? ?? ?? 6A 66 6A 6D 6A 03 E8 ?? ?? ?? ?? 83 C4 14 33 C0 5F 5E 5B 81 C4 A0 02 00 00 C3 8B 8C 24 B8 02 00 00 51 E8 ?? ?? ?? ?? 8B D8 83 C4 04 3B DE 89 5C 24 18 75 1C 8B 94 24 B0 02 00 00 6A 01 52 E8 ?? ?? ?? ?? 83 C4 08 5F 5E 5B 81 C4 A0 02 00 00 C3 55 8B AC 24 C4 02 00 00 55 E8 ?? ?? ?? ?? 55 E8 ?? ?? ?? ?? 8B F0 55 89 74 24 24 E8 }
	condition:
		$c0
}