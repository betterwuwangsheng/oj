import moment from "moment";

export function formatTime(arr: any) {
  arr.forEach((item: any) => {
    item.createTime = moment(item.createTime).format("YYYY-MM-DD HH:mm:ss");
  });
  return arr;
}
