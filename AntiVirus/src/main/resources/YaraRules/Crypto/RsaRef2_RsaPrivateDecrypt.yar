rule RsaRef2_NN_modInv
{	meta:
		author = "Maxx"
		description = "RsaRef2 NN_modInv"
	strings:
		$c0 = { 81 EC A4 04 00 00 53 56 8B B4 24 BC 04 00 00 57 8D 84 24 ?? 00 00 00 56 50 E8 ?? ?? ?? ?? 8D 8C 24 1C 01 00 00 BF 01 00 00 00 56 51 89 BC 24 A0 00 00 00 E8 ?? ?? ?? ?? 8B 94 24 C8 04 00 00 56 8D 84 24 AC 01 00 00 52 50 E8 ?? ?? ?? ?? 8B 9C 24 D8 04 00 00 56 8D 4C 24 2C 53 51 E8 ?? ?? ?? ?? 8D 54 24 34 56 52 E8 ?? ?? ?? ?? 83 C4 30 85 C0 0F 85 ED 00 00 00 8D 44 24 0C 56 50 8D 8C 24 A0 01 00 00 56 8D 94 24 AC 02 00 00 51 8D 84 24 34 03 00 00 52 50 E8 ?? ?? ?? ?? 8D 8C 24 2C 01 }
	condition:
		$c0
}