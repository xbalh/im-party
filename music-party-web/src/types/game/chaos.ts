export interface PersonFightInfo {
    agi: number,
    blk: string,
    blkPer: string,
    buff: Array<BuffInfo>,
    ca: string,
    cri: string,
    criPer: string,
    finalAtk: string,
    finalDef: string,
    hp: number,
    htl: number,
    name: string,
    spd: number,
    str: number,
    usedWeapon: Array<string>,
    wit: number,
    wpInfo: WpInfo
}

export interface BuffInfo {
    buff: string,
    turns: number
}

export interface WpInfo {
    skHolding: string,
    userName: string,
    wpHolding: string,
    wpNameHolding: string,
    wpDicList: Array<WpDic>,
}

export interface WpDic {
    wpStr: number;
    wpWit: number;
    wpAgi: number;
    wpHlt: number;
    wpType: string;
    wpName: string;
    wpRarity: string;
    wpSize: string;
    wpAtkType: string;
    wpDetail: string;
}

export interface BattleInfo {
    createTime: string,
    defenseInfo: PersonFightInfo,
    fightInfo: string,
    id: string,
    ifEnd: boolean,
    message: Array<string>,
    offensiveInfo: PersonFightInfo,
    round: number,
    surplusCd: string
}

export interface WpNodesInfo {
    imageUrl: NodeRequire,
    wpDic : WpDic
}
