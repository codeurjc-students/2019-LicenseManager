import { License } from "../licenses/license.model";

export interface LicenseStatistics{
	
	license:License;
	ip:string;
	nUsage:number;
	lastUsage:Date;
}