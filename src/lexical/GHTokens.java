package lexical;

public enum GHTokens {

		tEOF(0), id(2), tInt(4), tInts(5),	tFloat(6),
		tFloats(7),	tString(8),	tStrings(9), tBool(10),
		tBools(11), tCommit(12), tCommits(13), tUser(14),
		tUsers(15),	tRepository(16), tRepositories(17),
		tFile(18), tFiles(19), tIssue(20), tIssues(21),
		tPullRequest(22), tPullRequests(23), tComment(24),
		tComments(25), tDate(26), tDates(27), escStart(28),
		escEnd(29),	paramStart(30), paramEnd(31), cmt(32),
		term(33), point(34), in(35), prWrite(37), prIf(38),
		prElse(39),	prWhile(40), prFor(41), opAtrib(43),
		opIncre(44), opLogicAnd(45), opLogicOr(46),
		opLogicNeg(47),	opAritAd(48), opAritMul(49), opNegUn(50),
		opRel1(51), opRel2(52), opRel3(53), opConc(54),
		opAccess(55), tUnknown(56), fAdd(57), fRemove(58),
		fUpper(59),	fLower(60), fCollectData(61), fRefresh(62),
		fDiffDates(63), fLength(64), fCompare(65), fSort(66),
		tLitString(67), tLitInt(68), tLitFloat(69), tLitBool(70),
		comma(71);

	private int value;

	private GHTokens(int i){
		value = i;
	}
	
	public int getTokens(){
		return value;
	}
}
