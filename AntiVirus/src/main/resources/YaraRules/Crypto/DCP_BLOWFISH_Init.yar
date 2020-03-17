rule DCP_RIJNDAEL_EncryptECB {
	meta:
		author = "_pusher_"
		description = "Look for DCP RijnDael EncryptECB"
		date = "2016-07"
	strings:
		$c0 = { 53 56 57 55 83 C4 B4 89 0C 24 8D 74 24 08 8D 7C 24 28 80 78 30 00 75 16 B9 ?? ?? ?? ?? B2 01 A1 ?? ?? ?? ?? E8 ?? ?? ?? ?? E8 ?? ?? ?? ?? 8B 0A 89 0F 8B CA 83 C1 04 8B 09 8D 5F 04 89 0B 8B CA 83 C1 08 8B 09 8D 5F 08 89 0B 83 C2 0C 8B 12 8D 4F 0C 89 11 8B 50 58 83 EA 02 85 D2 0F 82 3B 01 00 00 42 89 54 24 04 33 D2 8B 0F 8B DA C1 E3 02 33 4C D8 5C 89 0E 8D 4F 04 8B 09 33 4C D8 60 8D 6E 04 89 4D 00 8D 4F 08 8B 09 33 4C D8 64 8D 6E 08 89 4D 00 8D 4F 0C 8B 09 33 4C D8 68 8D 5E 0C 89 0B 33 C9 8A 0E 8D 0C 8D }
	condition:
		$c0
}