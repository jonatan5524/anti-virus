rule Delphi_IntToStr {
	meta:
		author = "_pusher_"
		description = "Look for IntToStr function"
		date = "2016-04"
		version = "0.1"
	strings:
		$c0 = { 55 8B EC 81 C4 00 FF FF FF 53 56 8B F2 8B D8 FF 75 0C FF 75 08 8D 85 00 FF FF FF E8 ?? ?? ?? ?? 8D 95 00 FF FF FF 8B C6 E8 ?? ?? ?? ?? EB 0E 8B 0E 8B C6 BA ?? ?? ?? ?? E8 ?? ?? ?? ?? 8B 06 E8 ?? ?? ?? ?? 33 D2 8A D3 3B C2 72 E3 5E 5B 8B E5 5D C2 08 00 }
		//x64 rad
		$c1 = { 53 48 83 EC 20 48 89 CB 48 85 D2 7D 10 48 89 D9 48 F7 DA 41 B0 01 E8 ?? ?? ?? ?? EB 0B 48 89 D9 4D 33 C0 E8 ?? ?? ?? ?? 48 89 D8 48 83 C4 20 5B C3 }
	condition:
		any of them
}