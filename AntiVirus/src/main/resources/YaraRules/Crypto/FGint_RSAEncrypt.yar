rule FGint_ConvertBase256StringToHexString
{	meta:
		author = "_pusher_"
		date = "2015-05"
		description = "FGint ConvertBase256StringToHexString"
	strings:
		$c0 = { 55 8B EC 33 C9 51 51 51 51 51 51 53 56 57 8B F2 89 45 FC 8B 45 FC E8 ?? ?? ?? ?? 33 C0 55 68 ?? ?? ?? ?? 64 FF 30 64 89 20 8B C6 E8 ?? ?? ?? ?? 8B 45 FC E8 ?? ?? ?? ?? 8B F8 85 FF 0F 8E AB 00 00 00 C7 45 F8 01 00 00 00 8B 45 FC 8B 55 F8 8A 5C 10 FF 33 C0 8A C3 C1 E8 04 83 F8 0A 73 1E 8D 45 F4 33 D2 8A D3 C1 EA 04 83 C2 30 E8 ?? ?? ?? ?? 8B 55 F4 8B C6 E8 ?? ?? ?? ?? EB 1C 8D 45 F0 33 D2 8A D3 C1 EA 04 83 C2 37 E8 ?? ?? ?? ?? 8B 55 F0 8B C6 E8 ?? ?? ?? ?? 8B C3 24 0F 3C 0A 73 22 8D 45 EC 8B D3 80 E2 0F 81 E2 FF 00 00 00 83 C2 30 E8 ?? ?? ?? ?? 8B 55 EC 8B C6 E8 ?? ?? ?? ?? EB 20 8D 45 E8 8B D3 80 E2 0F 81 E2 FF 00 00 00 83 C2 37 }
	condition:
		$c0
}