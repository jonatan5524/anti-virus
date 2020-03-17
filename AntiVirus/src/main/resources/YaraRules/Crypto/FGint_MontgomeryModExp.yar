rule CryptoPP_modulo
{	meta:
		author = "Maxx"
		description = "CryptoPP modulo"
	strings:
		$c0 = { 83 EC 20 53 55 8B 6C 24 2C 8B D9 85 ED 89 5C 24 08 75 18 8D 4C 24 0C E8 ?? ?? ?? ?? 8D 44 24 0C 68 ?? ?? ?? ?? 50 E8 ?? ?? ?? ?? 8D 4D FF 56 85 CD 57 75 09 8B 53 04 8B 02 23 C1 EB 76 8B CB E8 ?? ?? ?? ?? 83 FD 05 8B C8 77 2D 33 F6 33 FF 49 85 C0 74 18 8B 53 04 8D 41 01 8D 14 8A 8B 0A 03 F1 83 D7 00 48 83 EA 04 85 C0 77 F1 6A 00 55 57 56 E8 ?? ?? ?? ?? EB 3B 33 C0 8B D1 49 85 D2 74 32 8B 54 24 10 33 DB 8D 71 01 8B 52 04 8D 3C 8A 8B 17 33 ED 0B C5 8B 6C 24 34 33 C9 53 0B CA 55 }
		$c1 = { 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 64 89 25 00 00 00 00 83 EC 2C 56 57 8B F1 33 FF 8D 4C 24 20 89 7C 24 08 E8 ?? ?? ?? ?? 8D 4C 24 0C 89 7C 24 3C E8 ?? ?? ?? ?? 8B 44 24 48 8D 4C 24 0C 50 56 8D 54 24 28 51 52 C6 44 24 4C 01 E8 ?? ?? ?? ?? 8B 74 24 54 83 C4 10 8D 44 24 20 8B CE 50 E8 ?? ?? ?? ?? 8B 7C 24 18 8B 4C 24 14 8B D7 33 C0 F3 AB 52 E8 ?? ?? ?? ?? 8B 7C 24 30 8B 4C 24 2C 8B D7 33 C0 C7 44 24 10 ?? ?? ?? ?? 52 F3 AB E8 ?? ?? ?? ?? 8B 4C 24 3C 83 C4 08 8B C6 64 89 }
		$c2 = { 83 EC 24 53 55 8B 6C 24 30 8B D9 85 ED 89 5C 24 08 75 18 8D 4C 24 0C E8 ?? ?? ?? ?? 8D 44 24 0C 68 ?? ?? ?? ?? 50 E8 ?? ?? ?? ?? 8D 4D FF 56 85 CD 57 75 09 8B 53 0C 8B 02 23 C1 EB 76 8B CB E8 ?? ?? ?? ?? 83 FD 05 8B C8 77 2D 33 F6 33 FF 49 85 C0 74 18 8B 53 0C 8D 41 01 8D 14 8A 8B 0A 03 F1 83 D7 00 48 83 EA 04 85 C0 77 F1 6A 00 55 57 56 E8 ?? ?? ?? ?? EB 3B 33 C0 8B D1 49 85 D2 74 32 8B 54 24 10 33 DB 8D 71 01 8B 52 0C 8D 3C 8A 8B 17 33 ED 0B C5 8B 6C 24 38 33 C9 53 0B CA 55 }
		$c3 = { 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 64 89 25 00 00 00 00 83 EC 1C 56 57 8B F1 33 FF 8D 4C 24 0C 89 7C 24 08 E8 ?? ?? ?? ?? 8D 4C 24 18 89 7C 24 2C E8 ?? ?? ?? ?? 8B 44 24 38 8D 4C 24 18 50 56 8D 54 24 14 51 52 C6 44 24 3C 01 E8 ?? ?? ?? ?? 8B 74 24 44 83 C4 10 8D 44 24 0C 8B CE 50 E8 ?? ?? ?? ?? 8B 4C 24 18 8B 7C 24 1C 33 C0 F3 AB 8B 4C 24 1C 51 E8 ?? ?? ?? ?? 8B 4C 24 10 8B 7C 24 14 33 C0 F3 AB 8B 54 24 14 52 E8 ?? ?? ?? ?? 8B 4C 24 2C 83 C4 08 8B C6 64 89 0D 00 00 00 }
	condition:
		any of them
}