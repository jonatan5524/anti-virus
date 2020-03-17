rule FGint_ECPointDestroy
{	meta:
		author = "_pusher_"
		date = "2015-06"
		description = "FGint ECPointDestroy"
		version = "0.1"
	strings:
		$c0 = { 53 8B D8 8B C3 E8 ?? ?? ?? ?? 8D 43 08 E8 ?? ?? ?? ?? 5B C3 }
	condition:
		$c0
}