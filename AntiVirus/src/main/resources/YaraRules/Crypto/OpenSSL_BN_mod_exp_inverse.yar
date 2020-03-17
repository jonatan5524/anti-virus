rule OpenSSL_BN_mod_exp_recp
{	meta:
		author = "Maxx"
		description = "OpenSSL BN_mod_exp_recp"
	strings:
		$c0 = { B8 C8 02 00 00 E8 ?? ?? ?? ?? 8B 84 24 D4 02 00 00 55 56 33 F6 50 89 74 24 1C 89 74 24 18 E8 ?? ?? ?? ?? 8B E8 83 C4 04 3B EE 89 6C 24 0C 75 1B 8B 8C 24 D4 02 00 00 6A 01 51 E8 ?? ?? ?? ?? 83 C4 08 5E 5D 81 C4 C8 02 00 00 C3 53 57 8B BC 24 EC 02 00 00 57 E8 ?? ?? ?? ?? 57 E8 ?? ?? ?? ?? 8B D8 83 C4 08 3B DE 0F 84 E7 02 00 00 8D 54 24 24 52 E8 ?? ?? ?? ?? 8B B4 24 EC 02 00 00 83 C4 04 8B 46 0C 85 C0 74 32 56 53 E8 ?? ?? ?? ?? 83 C4 08 85 C0 0F 84 BA 02 00 00 57 8D 44 24 28 53 }
	condition:
		$c0
}