// To parse this data:
//
//   import { Convert, Memory } from "./file";
//
//   const memory = Convert.toMemory(json);
//
// These functions will throw an error if the JSON doesn't
// match the expected interface, even if the JSON is valid.

export interface Memory {
  maxFreq?:                number;
  currentFreq?:            number[];
  contextSwitches?:        number;
  interrupts?:             number;
  systemCPULoadTicks?:     number[];
  processorCPULoadTicks?:  Array<number[]>;
  physicalPackageCount?:   number;
  physicalProcessorCount?: number;
  logicalProcessorCount?:  number;
  logicalProcessors?:      LogicalProcessor[];
  processorIdentifier?:    ProcessorIdentifier;
}

export interface LogicalProcessor {
  processorNumber?:         number;
  physicalProcessorNumber?: number;
  physicalPackageNumber?:   number;
  numaNode?:                number;
  processorGroup?:          number;
}

export interface ProcessorIdentifier {
  processorID?:       string;
  cpu64Bit?:          boolean;
  name?:              string;
  family?:            string;
  identifier?:        string;
  model?:             string;
  vendorFreq?:        number;
  vendor?:            string;
  stepping?:          string;
  microarchitecture?: string;
}

// Converts JSON strings to/from your types
// and asserts the results of JSON.parse at runtime
export class Convert {
  public static toMemory(json: string): Memory {
      return cast(JSON.parse(json), r("Memory"));
  }

  public static memoryToJson(value: Memory): string {
      return JSON.stringify(uncast(value, r("Memory")), null, 2);
  }
}

function invalidValue(typ: any, val: any, key: any = ''): never {
  if (key) {
      throw Error(`Invalid value for key "${key}". Expected type ${JSON.stringify(typ)} but got ${JSON.stringify(val)}`);
  }
  throw Error(`Invalid value ${JSON.stringify(val)} for type ${JSON.stringify(typ)}`, );
}

function jsonToJSProps(typ: any): any {
  if (typ.jsonToJS === undefined) {
      const map: any = {};
      typ.props.forEach((p: any) => map[p.json] = { key: p.js, typ: p.typ });
      typ.jsonToJS = map;
  }
  return typ.jsonToJS;
}

function jsToJSONProps(typ: any): any {
  if (typ.jsToJSON === undefined) {
      const map: any = {};
      typ.props.forEach((p: any) => map[p.js] = { key: p.json, typ: p.typ });
      typ.jsToJSON = map;
  }
  return typ.jsToJSON;
}

function transform(val: any, typ: any, getProps: any, key: any = ''): any {
  function transformPrimitive(typ: string, val: any): any {
      if (typeof typ === typeof val) return val;
      return invalidValue(typ, val, key);
  }

  function transformUnion(typs: any[], val: any): any {
      // val must validate against one typ in typs
      const l = typs.length;
      for (let i = 0; i < l; i++) {
          const typ = typs[i];
          try {
              return transform(val, typ, getProps);
          } catch (_) {}
      }
      return invalidValue(typs, val);
  }

  function transformEnum(cases: string[], val: any): any {
      if (cases.indexOf(val) !== -1) return val;
      return invalidValue(cases, val);
  }

  function transformArray(typ: any, val: any): any {
      // val must be an array with no invalid elements
      if (!Array.isArray(val)) return invalidValue("array", val);
      return val.map(el => transform(el, typ, getProps));
  }

  function transformDate(val: any): any {
      if (val === null) {
          return null;
      }
      const d = new Date(val);
      if (isNaN(d.valueOf())) {
          return invalidValue("Date", val);
      }
      return d;
  }

  function transformObject(props: { [k: string]: any }, additional: any, val: any): any {
      if (val === null || typeof val !== "object" || Array.isArray(val)) {
          return invalidValue("object", val);
      }
      const result: any = {};
      Object.getOwnPropertyNames(props).forEach(key => {
          const prop = props[key];
          const v = Object.prototype.hasOwnProperty.call(val, key) ? val[key] : undefined;
          result[prop.key] = transform(v, prop.typ, getProps, prop.key);
      });
      Object.getOwnPropertyNames(val).forEach(key => {
          if (!Object.prototype.hasOwnProperty.call(props, key)) {
              result[key] = val[key];
          }
      });
      return result;
  }

  if (typ === "any") return val;
  if (typ === null) {
      if (val === null) return val;
      return invalidValue(typ, val);
  }
  if (typ === false) return invalidValue(typ, val);
  while (typeof typ === "object" && typ.ref !== undefined) {
      typ = typeMap[typ.ref];
  }
  if (Array.isArray(typ)) return transformEnum(typ, val);
  if (typeof typ === "object") {
      return typ.hasOwnProperty("unionMembers") ? transformUnion(typ.unionMembers, val)
          : typ.hasOwnProperty("arrayItems")    ? transformArray(typ.arrayItems, val)
          : typ.hasOwnProperty("props")         ? transformObject(getProps(typ), typ.additional, val)
          : invalidValue(typ, val);
  }
  // Numbers can be parsed by Date but shouldn't be.
  if (typ === Date && typeof val !== "number") return transformDate(val);
  return transformPrimitive(typ, val);
}

function cast<T>(val: any, typ: any): T {
  return transform(val, typ, jsonToJSProps);
}

function uncast<T>(val: T, typ: any): any {
  return transform(val, typ, jsToJSONProps);
}

function a(typ: any) {
  return { arrayItems: typ };
}

function u(...typs: any[]) {
  return { unionMembers: typs };
}

function o(props: any[], additional: any) {
  return { props, additional };
}

function m(additional: any) {
  return { props: [], additional };
}

function r(name: string) {
  return { ref: name };
}

const typeMap: any = {
  "Memory": o([
      { json: "maxFreq", js: "maxFreq", typ: u(undefined, 0) },
      { json: "currentFreq", js: "currentFreq", typ: u(undefined, a(0)) },
      { json: "contextSwitches", js: "contextSwitches", typ: u(undefined, 0) },
      { json: "interrupts", js: "interrupts", typ: u(undefined, 0) },
      { json: "systemCpuLoadTicks", js: "systemCPULoadTicks", typ: u(undefined, a(0)) },
      { json: "processorCpuLoadTicks", js: "processorCPULoadTicks", typ: u(undefined, a(a(0))) },
      { json: "physicalPackageCount", js: "physicalPackageCount", typ: u(undefined, 0) },
      { json: "physicalProcessorCount", js: "physicalProcessorCount", typ: u(undefined, 0) },
      { json: "logicalProcessorCount", js: "logicalProcessorCount", typ: u(undefined, 0) },
      { json: "logicalProcessors", js: "logicalProcessors", typ: u(undefined, a(r("LogicalProcessor"))) },
      { json: "processorIdentifier", js: "processorIdentifier", typ: u(undefined, r("ProcessorIdentifier")) },
  ], false),
  "LogicalProcessor": o([
      { json: "processorNumber", js: "processorNumber", typ: u(undefined, 0) },
      { json: "physicalProcessorNumber", js: "physicalProcessorNumber", typ: u(undefined, 0) },
      { json: "physicalPackageNumber", js: "physicalPackageNumber", typ: u(undefined, 0) },
      { json: "numaNode", js: "numaNode", typ: u(undefined, 0) },
      { json: "processorGroup", js: "processorGroup", typ: u(undefined, 0) },
  ], false),
  "ProcessorIdentifier": o([
      { json: "processorID", js: "processorID", typ: u(undefined, "") },
      { json: "cpu64bit", js: "cpu64Bit", typ: u(undefined, true) },
      { json: "name", js: "name", typ: u(undefined, "") },
      { json: "family", js: "family", typ: u(undefined, "") },
      { json: "identifier", js: "identifier", typ: u(undefined, "") },
      { json: "model", js: "model", typ: u(undefined, "") },
      { json: "vendorFreq", js: "vendorFreq", typ: u(undefined, 0) },
      { json: "vendor", js: "vendor", typ: u(undefined, "") },
      { json: "stepping", js: "stepping", typ: u(undefined, "") },
      { json: "microarchitecture", js: "microarchitecture", typ: u(undefined, "") },
  ], false),
};
