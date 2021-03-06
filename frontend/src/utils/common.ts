interface Object {
    [key: string]: string | File | null;
}

export const createFormData = (data: Object) => {
    const formData = new FormData();
    Object.keys(data).forEach((key) => formData.append(key, data[key]!));
    return formData;
};

export const YYYYMMDDToDate = (str: string) => new Date(str.substring(0, 10));

export const getRandomColor = () => {
    const letters = '0123456789abcdef';
    return Array.from({ length: 6 }, (v, i) => i).reduce((prev) => prev + letters[Math.floor(Math.random() * 16)], '#');
};

export const getColorFromColorMap = (key: string, colors: Map<string, string>) => {
    if (!key) return;
    if (colors.has(key)) {
        return colors.get(key)!;
    }
    colors.set(key, getRandomColor());
    return colors.get(key)!;
};

export const isSameDate = (date1: Date, date2: Date) =>
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getDate() === date2.getDate();
