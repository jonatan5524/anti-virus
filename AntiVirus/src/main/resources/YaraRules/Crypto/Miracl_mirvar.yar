rule RsaEuro_NN_modMult
{	meta:
		author = "Maxx"
		description = "RsaEuro NN_modMult"
	strings:
		$c0 = { 8B 44 24 0C 8B 4C 24 08 81 EC 08 01 00 00 8D 54 24 00 56 8B B4 24 20 01 00 00 56 50 51 52 E8 ?? ?? ?? ?? 8B 84 24 2C 01 00 00 56 8D 0C 36 50 8B 84 24 28 01 00 00 8D 54 24 1C 51 52 50 E8 ?? ?? ?? ?? 83 C4 24 5E 81 C4 08 01 00 00 C3 }
	condition:
		$c0
}