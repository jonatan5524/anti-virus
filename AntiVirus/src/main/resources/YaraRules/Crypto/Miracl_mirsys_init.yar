rule Miracl_Big_constructor
{	meta:
		author = "Maxx"
		description = "Miracl Big constructor"
	strings:
		$c0 = { 56 8B F1 6A 00 E8 ?? ?? ?? ?? 83 C4 04 89 06 8B C6 5E C3 }
	condition:
		$c0
}