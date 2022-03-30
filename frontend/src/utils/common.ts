interface Object {
    [key: string]: string | File | null;
}

export const createFormData = (data: Object) => {
    const formData = new FormData();
    Object.keys(data).forEach((key) => formData.append(key, data[key]!));
    return formData;
};

export const YYYYMMDDToDate = (str: string) => {
    const numStr = str.replace(/[^\d]/g, '');

    const date = new Date(
        parseInt(numStr.slice(0, 4), 10),
        parseInt(numStr.slice(4, 6), 10) - 1,
        parseInt(numStr.slice(6, 8), 10),
    );
    return date;
};

export const getRandomColor = () => {
    const letters = '0123456789abcdef';
    return Array.from({ length: 6 }, (v, i) => i).reduce((prev) => prev + letters[Math.floor(Math.random() * 16)], '#');
};

export const getColorFromColorMap = (key: string, colors: Map<string, string>) => {
    if (colors.has(key)) {
        return colors.get(key)!;
    }
    colors.set(key, getRandomColor());
    return colors.get(key)!;
};
