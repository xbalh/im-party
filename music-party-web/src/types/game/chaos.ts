export interface PersonFightInfo {
    agi: number,
    blk: number,
    blkPer: number,
    buff: Array<BuffInfo>,
    ca: number,
    cri: number,
    criPer: number,
    finalAtk: number,
    finalDef: number,
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
    wpHolding: string
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

