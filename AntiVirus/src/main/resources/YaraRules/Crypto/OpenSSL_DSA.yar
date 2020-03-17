rule OpenSSL_BN_mod_exp_simple
{	meta:
		author = "Maxx"
		description = "OpenSSL BN_mod_exp_simple"
	strings:
		$c0 = { B8 98 02 00 00 E8 ?? ?? ?? ?? 8B 84 24 A4 02 00 00 55 56 33 ED 50 89 6C 24 1C 89 6C 24 18 E8 ?? ?? ?? ?? 8B F0 83 C4 04 3B F5 89 74 24 0C 75 1B 8B 8C 24 A4 02 00 00 6A 01 51 E8 ?? ?? ?? ?? 83 C4 08 5E 5D 81 C4 98 02 00 00 C3 53 57 8B BC 24 BC 02 00 00 57 E8 ?? ?? ?? ?? 57 E8 ?? ?? ?? ?? 8B D8 83 C4 08 3B DD 0F 84 71 02 00 00 8D 54 24 28 52 E8 ?? ?? ?? ?? 8B AC 24 BC 02 00 00 8B 84 24 B4 02 00 00 57 55 8D 4C 24 34 50 51 C7 44 24 30 01 00 00 00 E8 ?? ?? ?? ?? 83 C4 14 85 C0 0F }
	condition:
		$c0
}